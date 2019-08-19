@if not exist ./build.xml if exist ../build.xml cd ..
docker-compose -f docker-compose2.yml down
docker ps -a
