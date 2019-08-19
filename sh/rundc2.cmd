@if not exist ./build.xml if exist ../build.xml cd ..
@rem call ant docker2
@if errorlevel 1 goto finish
@setlocal
@if .%1 == .-d set ARGS=-d
docker-compose -f docker-compose2.yml up %ARGS%
@if .%1 == . docker ps
:finish
