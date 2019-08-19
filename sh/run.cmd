taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGateway"
@if not exist ./build.xml if exist ../build.xml cd ..
call ant build
@if errorlevel 1 goto finish
@if NOT .%1 == .-i goto window
java -jar deploy/paymentgateway-all.jar
goto finish
:window
start "PaymentGateway" java -jar deploy/paymentgateway-all.jar
:finish
