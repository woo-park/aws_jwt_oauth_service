#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=aws_jwt_oauth_service

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl aws_jwt_oauth_service | grep jar | awk '{print $1}')
#TEST_CURRENT_PID=$(pgrep -fl aws_jwt_oauth_service)

echo "현재 구동중인 어플리케이션 pid: $CURRENT_PID"
#echo "현재 테스트 pid: $TEST_CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application.yml,/home/ec2-user/app/application-oauth.properties \
  -Dspring.profiles.active=oauth \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
#  $REPOSITORY/$JAR_NAME 2>&1 &


# step 1에서 작성된 deploy.sh와 크게 다르지 않습니다. 우선 git pull을 통해 직접 빌드했던 부분을 제거했습니다.