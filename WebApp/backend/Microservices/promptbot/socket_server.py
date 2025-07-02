# ── socket_server.py ─────────────────────────────────────────────
"""
Socket.IO server (polling-only) that forwards status messages
from promptbot → browser, ONE client at a time.

Start with:  python socket_server.py
"""

import socketio
import threading
from queue import Empty
from wsgiref import simple_server

# ── your own modules ────────────────────────────────────────────
from promptbot import run as bot_run               # the bots main loop
from shared_queue import response_status_queue     # Queue shared by bot + server

# ── socket.io setup ─────────────────────────────────────────────
sio = socketio.Server(
    cors_allowed_origins="*",
    async_mode="threading",     # ← no greenlets, just OS threads
    allow_upgrades=False        # ← polling-only keeps a single sid
)
app = socketio.WSGIApp(sio)

active_client_sid = None       # tracks the single connected browser


# ── Socket.IO event handlers ────────────────────────────────────
@sio.event
def connect(sid, environ):
    """A browser tab connected."""
    global active_client_sid
    active_client_sid = sid
    print(f"[connect]  new client SID = {sid}")
    response_status_queue.put(f"Connection established with WebSocket server - {sid}")


@sio.event
def disconnect(sid):
    """Browser tab closed or reloaded."""
    global active_client_sid
    print(f"[disconnect] client SID = {sid}")
    if sid == active_client_sid:
        active_client_sid = None


# ── Background thread that relays Queue → Socket.IO ─────────────
def emit_status():
    """Continuously pop messages from the queue and emit to the client."""
    global active_client_sid
    while True:
        try:
            msg = response_status_queue.get(timeout=2)
        except Empty:
            continue

        if active_client_sid:
            print(f"[emit]      to {active_client_sid}: {msg}")
            sio.emit("status", msg, to=active_client_sid)
        else:
            print(f"[emit]  ⚠ no active client, dropped: {msg}")


# ── Main ────────────────────────────────────────────────────────
if __name__ == "__main__":
    # 1. start the bot in its own daemon thread
    threading.Thread(target=bot_run, daemon=True).start()
    print("[main] promptbot thread started")

    # 2. start the status-emitter thread
    threading.Thread(target=emit_status, daemon=True).start()
    print("[main] emitter thread started")

    # 3. run a blocking WSGI server (suitable for dev)
    print("[main] Socket.IO server listening on http://0.0.0.0:5000")
    simple_server.make_server("0.0.0.0", 5000, app).serve_forever()
