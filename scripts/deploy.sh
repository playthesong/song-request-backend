#!/bin/bash

REPOSITORY=/home/ubuntu/app/songrequest
ENVIRONMENT_PATH=/home/ubuntu/.bashrc
PROJECT_NAME=song-request

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl song-request | grep java | awk '{print $1}')

echo "현재 구동 중인 애플리케이션 pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

echo "> $JAR_NAME에 실행 권한 추가"

chmod +x $JAR_NAME

echo "> 환경 변수 추가"

chmod +x $ENVIRONMENT_PATH

source $ENVIRONMENT_PATH

echo "> $JAR_NAME 실행"

nohup java -jar \
  -Dspring.profiles.active=prod \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
