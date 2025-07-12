@echo off
setlocal
set "ROOT=%~dp0"

wt -w new ^
  new-tab --title "Prompt consumer"          cmd /k "cd /d \"%ROOT%promptbot\" && python main.py" ^;^
  new-tab --title "Data-storage subscriber"  cmd /k "cd /d \"%ROOT%redis_sub_for_data_storage\" && python main.py" ^;^
  new-tab --title "Test publisher"           cmd /k "python \"%ROOT%redis_testing.py\""