#!/usr/bin/env python
# ──────────────────────────────────────────────────────────────────────────────
#  prompt_producer.py
#
#  Interactive helper that pushes messages onto the Redis list
#  called  prompt-queue.  Your Java/Spring worker pops (BLPOP) from the
#  **left**, so we push (RPUSH) onto the **right** to keep FIFO order.
#
#  $ pip install redis==5.*         # if not already installed
#  $ export REDIS_HOST=localhost    # adjust if needed
#  $ export REDIS_PORT=6379
#  $ python prompt_producer.py
#
#  Press Ctrl-C to quit.
# ──────────────────────────────────────────────────────────────────────────────

import os
import sys
import json
import signal
from datetime import datetime, timezone
from typing import Final, TypedDict

import redis                        # redis-py 5.x+

# ──────────────────────────────────────────────────────────────────────────────
# 1.  Connection parameters (override with env vars)
# ──────────────────────────────────────────────────────────────────────────────
REDIS_HOST: Final[str] = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT: Final[int] = int(os.getenv("REDIS_PORT", "6379"))
QUEUE_KEY: Final[str] = "prompt-queue"          # must match the Java worker

try:
    rdb: redis.Redis = redis.Redis(
        host=REDIS_HOST,
        port=REDIS_PORT,
        decode_responses=True,      # str ↔ str instead of bytes
    )
    rdb.ping()
except redis.ConnectionError as err:
    sys.exit(f"Cannot reach Redis @ {REDIS_HOST}:{REDIS_PORT} - {err}")

print(f"Connected to Redis {REDIS_HOST}:{REDIS_PORT}")
print("Messages will be RPUSHed to list  prompt-queue .\n")


# ──────────────────────────────────────────────────────────────────────────────
# 2.  Ctrl-C handler (clean exit)
# ──────────────────────────────────────────────────────────────────────────────
def _exit(*_):
    sys.exit(0)

signal.signal(signal.SIGINT, _exit)


# ──────────────────────────────────────────────────────────────────────────────
# 3.  Optional strong typing for the JSON envelope
# ──────────────────────────────────────────────────────────────────────────────
class PromptMsg(TypedDict):
    prompt: str
    user_id: int
    ts: str            # ISO-8601 timestamp


# ──────────────────────────────────────────────────────────────────────────────
# 4.  REPL loop - read prompt & user id, push JSON blob
# ──────────────────────────────────────────────────────────────────────────────
while True:
    try:
        prompt_text = input("Prompt: ").strip()
        if not prompt_text:
            print("  (empty prompt - skipped)\n")
            continue

        user_text = input("User ID (int): ").strip()
        if not user_text.isdigit():
            print("  (user id must be an integer - skipped)\n")
            continue

        msg: PromptMsg = {
            "prompt": prompt_text,
            "user_id": int(user_text),
            "ts": datetime.now(tz=timezone.utc).isoformat(timespec="seconds"),
        }

        # Serialize to JSON & push to the RIGHT end → worker pops from LEFT
        rdb.rpush(QUEUE_KEY, json.dumps(msg))

        print(f"Enqueued → {msg}\n")

    except Exception as e:                              # keep the loop alive
        print(f"Error: {e}\n")
