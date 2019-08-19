taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGateway"
@if not exist ./build.xml if exist ../build.xml cd ..
call ant build
@if errorlevel 1 goto finish
@setlocal
@if .%1 == . goto run
@if exist %1 set TestFile=%~nx1 & goto run
@if exist src/test/cucumber/%1 set TestFile=%1 & goto run
@if exist src/test/cucumber/%1.feature set TestFile=%1.feature & goto run
:run
@if exist \ansicon\launch.cmd call \ansicon\launch.cmd -p
java -cp tests/lib/*;deploy/paymentgateway-all.jar cucumber.api.cli.Main --glue acceptance.stepdefs tests/cucumber/%TestFile% --plugin pretty --tags ~@UI --tags ~@API --snippets camelcase
:finish
