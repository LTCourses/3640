taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGateway"
@if not exist ./build.xml if exist ../build.xml cd ..
call ant build
@if errorlevel 1 goto finish
@setlocal
@if .%1 == . set TestFile=*.side & goto run
@if exist %1 set TestFile=%~nx1 & goto run
@if exist src/test/selenium/%1 set TestFile=%1 & goto run
@if exist src/test/selenium/%1.side set TestFile=%1.side & goto run
@set TestFile=*.side
:run
start "PaymentGateway" java -DPG_EXPIRY=fix -jar deploy/paymentgateway-all.jar
java -jar tests/lib/selenese-runner.jar --geckodriver tests/drivers/geckodriver.exe --xml-result results/selenium --html-result report/selenium --screenshot-on-fail results/selenium tests/selenium/%TestFile%
:finish
