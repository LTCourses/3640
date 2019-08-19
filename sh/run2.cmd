taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGatewayUi"
taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGatewayApi2"
@if not exist ./build.xml if exist ../build.xml cd ..
call ant build2
@if errorlevel 1 goto finish
start "PaymentGatewayUi" java -jar deploy/paymentgateway-ui.jar
start "PaymentGatewayApi2" java -jar deploy/paymentgateway-api2.jar
:finish
