# JDA를 활용한 Discord Bot 만들기
![Static Badge](https://img.shields.io/badge/Java-3766AB)
 ![Static Badge](https://img.shields.io/badge/DiscordApi-JDA-8A2BE2) 

# Discord 識
![image](https://github.com/UNGGU0704/java_discord_bot/assets/130115689/02987509-bb25-412f-a919-3b70f6efe4ad)





본 프로그램은 오픈소스 api인 *[jda](https://github.com/discord-jda/JDA)* 를 활용해서 만든 Java기반 Discord Bot 입니다.

**개발 기간** 
 2022. 08 ~ 2022. 09
 
**리팩토리 중** 
 2023. 12 ~

## 기능 
 - 팀 나누기
 - 게임 할 인원 모으기
- 욕설 필터링
- 밴 기능
- 링크 저장 기능

## 데이터 저장
- JSON으로 user 정보 저장

## 파일 구조

```

└─src
    └─main
        └─java
            └─Bot
                ├─chatBot
                │  ├─Main
                │  │      Main.java
                │  │      
                │  └─Message
                │      ├─Filter
                │      │      MessageValidator.java
                │      │      MesssagegFilter.java
                │      │      NegativeWords.java
                │      │      
                │      ├─Print
                │      │      Print.java
                │      │      
                │      └─Processor
                │              MessageProcessor.java
                │              
                ├─data
                │      UserData.java
                │      UserDTO.java
                │      UserRepository.java
                │      
                └─features
                    ├─Command
                    │  │  BasicCommand.java
                    │  │  FrontConmmand.java
                    │  │  
                    │  ├─Help
                    │  │      HelpInfoCommand.java
                    │  │      HelpSpecialCommand.java
                    │  │      HelpValorantCommand.java
                    │  │      
                    │  ├─Link
                    │  │      LinkCommand.java
                    │  │      
                    │  ├─Special
                    │  │      exampleCommand.java
                    │  │      YongJunBan.java
                    │  │      
                    │  └─Valorant
                    │          ValorantCommand.java
                    │          ValorantExecute.java
                    │          
                    └─Functions
                            Ban.java
                            
```
