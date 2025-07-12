@echo off
setlocal
set "ROOT=%~dp0"

wt -w new ^
  new-tab --title "Frontend"                 cmd /k "cd /d \"%ROOT%frontend\"                          && npm run dev" ^;^
  new-tab --title "Backend"                  cmd /k "cd /d \"%ROOT%backend\closedai\"                  && mvn spring-boot:run" ^;^
  new-tab --title "Prompt consumer"          cmd /k "cd /d \"%ROOT%microservices\promptbot\"           && python main.py" ^;^
  new-tab --title "Data-storage subscriber"  cmd /k "cd /d \"%ROOT%microservices\redis_sub_for_data_storage\" && python main.py" ^;^
  new-tab --title "Test publisher"           cmd /k "cd /d \"%ROOT%microservices\"                     && python redis_testing.py"
endlocal