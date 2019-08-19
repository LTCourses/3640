taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGatewayUi"
taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGatewayVisa"
taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGatewayMasterCard"
taskkill /f /im java.exe /fi "WINDOWTITLE eq PaymentGatewayOther"
@if not exist ./build.xml if exist ../build.xml cd ..
call ant build3
@if errorlevel 1 goto finish
start "PaymentGatewayUi" java -jar deploy/paymentgateway-ui.jar
start "PaymentGatewayVisa" java -DPG_PORT_API_OTHER=5554 -jar deploy/paymentgateway-api.jar
start "PaymentGatewayMasterCard" java -DPG_ISSUER=MASTERCARD -DPG_PORT_API=5554 -jar deploy/paymentgateway-api.jar
start "PaymentGatewayOther" java -jar deploy/paymentgateway-other.jar
:finish
