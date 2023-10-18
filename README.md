# Cooking
22-2 네트워크 프로그래밍

## 게임 설명
### 개요
![image](https://user-images.githubusercontent.com/61658477/206434552-9dc0edd7-f45d-43af-b00e-140dc4b874a3.png)

을 모티브로 제작한 4인 협동 요리 게임 입니다.

### 인게임 스샷
![preview](https://user-images.githubusercontent.com/61658477/206435614-a8127e06-c86f-49f8-bdaa-7c09a3c60690.png)

### 플레이 영상
[![Video Label](http://img.youtube.com/vi/iLZbx9MS6pw/0.jpg)](https://www.youtube.com/watch?v=iLZbx9MS6pw)

## 게임 설명

### 조작
WASD, 방향표: 움직이기

스페이스바: 블럭과 상호작용

Q: 음식 오더 보이기/숨기기

E: 메뉴얼 보이기/숨기기

### 재료
음식을 스페이스바를 통해 들수 있습니다.

각각의 재료는 알맞는 조리 기구에만 놓을 수 있습니다.

### 조리기구
조리기구 앞에서 음식을 들고 스페이스바를 통해 음식을 놓을 수 있습니다.

조리기구 마다 음식을 조리하는 방법이 약간 씩 다릅니다.

조리기구가 일정 시간마다 ! 표시가 뜰 때, 스페이스바 버튼을 눌러야 다시 동작합니다.

### 재료 별 조리 방법
![manual](https://user-images.githubusercontent.com/61658477/206434895-119c9eaa-edba-4615-a9fe-0a8fbdb31834.png)

해당 메뉴얼은 인 게임에서 E 버튼을 통해 보이기/숨기기가 가능합니다.

### 추가적으로..

쓰레기통: 붉은 뚜껑을 가진 상자, 음식을 버립니다.

테이블: 남색 타일 해당, 음식을 올려둘 수 있습니다.


### 오더
주문을 일정 시간마다 서버에서 클라이언트로 보내집니다.

주문을 보내는 시간과 주문이 만료되는 시간은 서버에서 추가 설정이 가능합니다.

## 플레이 방법

### 실행
1. CookingServer.jar을 실행 시킵니다.
2. Server Start 버튼을 눌러 서버를 시작합니다.
4. CookingClient.jar을 실행 시킵니다.
5. 입장하기 버튼을 눌러 시작합니다.

### 주의 사항
1. 자바 16 이상을 필요로 합니다.
2. 클라이언트의 접속 포트 넘버는 42021로 고정되어 있어있습니다.
3. 다른 플레이어가 같은 블록에서 동시에 상호작용 시 매우 높은 확률로 게임이 멈춤니다.

## 기타
해당 프로젝트의 일부 코드는 다음 프로젝트를 기반으로 제작하였습니다.

https://github.com/MarcoNadalin/2D_TopDown_TileBased_Java_Game
