@rem if not exist ./build.xml if exist ../build.xml cd ..
@rem call ant docker2
docker run -d --rm -p 5651:5551 -e PG_PORT_UI_API=5652 --name pg-ui pg-ui
docker run -d --rm -p 5652:5552 --name pg-api2 pg-api2
docker ps
