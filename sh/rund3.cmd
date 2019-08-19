@rem if not exist ./build.xml if exist ../build.xml cd ..
@rem call ant docker3
docker run -d --rm -p 5651:5551 -e PG_PORT_UI_API=5652 --name pg-ui pg-ui
docker run -d --rm -p 5652:5552 -e PG_PORT_API_OTHER=5653 -e PG_HOSTNAME_OTHER=%COMPUTERNAME% --name pg-visa pg-api
docker run -d --rm -p 5653:5553 --name pg-other pg-other
docker ps
