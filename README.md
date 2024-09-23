# Part 2. 만들면서 배우는 스프링

## @MVC 구현하기

### 학습목표
- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### 필요 요구사항
- build.gradle을 참고하여 환경 설정은 스스로 찾아서 한다.
- 미션을 시작할 때, `해당 기수(앞년도만) + 자신의 영문 이름`으로 브랜치를 파고, 작업 후 각자 브랜치로 커밋, 푸시한다.(예: `24YoonByungWook`)
- `main` 브랜치는 절대 건들지 말아주세요!!
- 예시
```text
git status
- On branch main
git branch 24YoonByungWook
git checkout 24YoonByungWook
```

# 0단계 - Reflection, DI 학습 미션
+ [ ] study 디렉토리 안의 test를 아래 순서대로 모두 수행한다.
+ [ ] study/src/test/java/reflection 디렉토리 내부의 테스트를 수행한다.
   + [ ] [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
   + [ ] [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
   + [ ] [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
   + [ ] [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
+ [ ] study/src/test/java/servlet 디렉토리 내부의 테스트를 수행한다.

# 1단계 - 0단계가 완료되면 공개됩니다.