@rem if not exist ./build.xml if exist ../build.xml cd ..
@rem call ant docker
@if errorlevel 1 goto finish
@setlocal
@if .%1 == .-d set ARGS=-d
docker run %ARGS% --rm -p 5651:5551 -p 5652:5552 -e PG_PORT_UI_API=5652 --name pg pg
@if .%1 == . docker ps
:finish
