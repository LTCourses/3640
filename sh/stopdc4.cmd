@if not exist ./build.xml if exist ../build.xml cd ..
docker-compose -f docker-compose4.yml down
docker ps -a
