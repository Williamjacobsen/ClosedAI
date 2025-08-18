#!/usr/bin/env bash
set -euo pipefail

SESSION="${SESSION:-project}"
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Start fresh
tmux kill-session -t "$SESSION" 2>/dev/null || true

# First window: NeoVim
tmux new-session -d -s "$SESSION" -n "NeoVim" -c "$ROOT"
tmux send-keys -t "$SESSION:NeoVim" "nvim ." C-m

# Frontend
tmux new-window -t "$SESSION" -n "Frontend" -c "$ROOT/frontend"
tmux send-keys  -t "$SESSION:Frontend" "npm run dev" C-m

# Backend2
tmux new-window -t "$SESSION" -n "Backend2" -c "$ROOT/backend2/closedai"
tmux send-keys  -t "$SESSION:Backend2" "mvn spring-boot:run" C-m

# Prompt consumer
tmux new-window -t "$SESSION" -n "Prompt consumer" -c "$ROOT/microservices/promptbot"
tmux send-keys  -t "$SESSION:Prompt consumer" "python main.py" C-m

# Data-storage subscriber
tmux new-window -t "$SESSION" -n "Data-storage sub" -c "$ROOT/microservices/redis_sub_for_data_storage"
tmux send-keys  -t "$SESSION:Data-storage sub" "python main.py" C-m

# Test publisher
tmux new-window -t "$SESSION" -n "Test publisher" -c "$ROOT/microservices"
tmux send-keys  -t "$SESSION:Test publisher" "python redis_testing.py" C-m

# Focus NeoVim
tmux select-window -t "$SESSION:NeoVim"

# Attach or switch depending on environment
if [ -n "${TMUX-}" ]; then
  tmux switch-client -t "$SESSION"
else
  tmux attach-session -t "$SESSION"
fi

