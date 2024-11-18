# Reflection API
## Java.lang.Class

코드 상에서 호출 로직을 통해 클래스 정보를 얻어와야 할 때 사용하는 객체

자신이 속한 클래스의 모든 멤버 정보를 담아 런타임 환경에서 동적으로 지정된 클래스나 인터페이스 정보를 가져온다.

이는 자바의 실행 방식과 연관이 있는데 자바의 모든 클래스와 인터페이스는 실행 시 바이트코드(.class)로 컴파일된다. java.lang.Class는 전역 생성자가 존재하지 않는 대신에 클래스가 로드되거나 defineClass 메서드가 클래스 로더에 의해 호출될 때 JVM에 의해 자동적으로 생성된다.

```java
void printClassName(Object obj){
	System.out.println("The class of " + obj + " is " + obj.getClass().getName());
}
```

### Class 객체를 얻는 방법

1. Object.getClass()

   모든 클래스의 최상위 클래스인 Object 클래스의 getClass() 메서드

   해당 클래스가 인스턴스화 된 상태여야 한다.

2. .class 리터럴

   인스턴스가 존재하지 않을 경우 사용한다.

3. Class.forName()

   동적 로딩

   JVM에서 실행에 필요한 모든 클래스 파일을 메모리에 올려놓지 않고, 필요한 시점에 동적으로 메모리에 올리는 기술이다.

   컴파일 타임에 체크 할 수 없기 때문에 클래스 유무가 확인되지 않아 예외 처리가 반드시 필요하다.


### Class의 메서드

- 클래스 정보
    - getSimpleName
    - getPackage
    - getName
    - toString
    - toGenricString
- 클래스 구성
    - getFields
    - getMethods
    - getInterfaces
    - getSuperclass

## Reflection API

구체적인 클래스 타입을 알지 못한 상태로 그 클래스의 정보에 접근하게 해주는 자바 기법

클래스의 파일의 위치나 이름만 있다면 해당 클래스의 정보를 얻어내고, 객체를 생성하는 것이 가능해진다.

### Reflection 사용법

- 동적으로 생성자 가져와 초기화하기

    ```java
    public static void main(String[] args) throws Exception{
    	Class<Person> personClass = (Class<Person>) Class.forNmae("Person");
    	
    	Constructor<Person> constructor = personClass.getConstructor(String.class, int.class);
    	
    	Person person1 = constructor.newInstance("홍길동", 55);
    	person1.getField();
    ```

    - getConstructor: 인자로 생성자의 매개변수 타입을 받아(바인딩) 해당하는 생성자를 리턴한다.
- 동적으로 메서드 가져와 실행하기

    ```java
    public static void main(String[] args) throws Exception{
    	Class<Person> personClass = (Class<Person>) Class.forName("Person");
    	
    	Method sum = personClass.getMethod("sum", int.class, int.class);
    	int result = (int)sum.invoker(new Person(), 10, 20);
    	System.out.println("result = "+result);
    	
    	Method staticSum = personClass.getMethod("staticSum", int.class, int.class);
    	int staticResult = (int)staticSum.invoke(null, 100, 200);
    	
    	Method privateSum = personClass.getDeclaredMethod("privateSum", int.class, int.class);
    	privateSum.setAccesible(true);
    	int privateResult = (int) privateSum.invoke(new Person(), 1000, 2000);
    	System.out.println("privateResult = "+privateResult);
    }
    ```

    - getMethod: 메서드 이름과 매개변수 타입을 받아 해당 메서드를 리턴한다. (public only)
    - getDeclareMethod: private 메서드를 가져온다.
        - setAccessible: private이기 때문에 외부에서 access 할 수 있도록 허용해주어야한다.
    - invoke: 가져온 메서드를 동적으로 실행시켜주는 메서드

      첫번째 파라미터로 메서드를 실행할 객체를 받고, 두번째 파라미터부터는 해당 메서드의 파라미터 값을 전달받는다.

- 동적으로 필드 가져와 조작하기

  필드 값을 원하는대로 변경할 수 있고 필드는 클래스가 인스턴스가 되어야 Heap 메모리에 적재되므로 인스턴스가 필요하다(static의 경우 이미 적재되어 있으므로 인스턴스가 필요하지 않다.).

    ```java
    public static void main(String[] args) throws Exception{
    	Class<Person> personClass = (Class<Person>) Class.forName("Person");
    	
    	Field height_field = personClass.getField("height");
    	height_field.set(null, 200);
    	System.out.println(height_field.get(null));
    }
    ```

    ```java
    public static void main(String[] args) throws Exception{
    	Person person = new Person("홍길동", 55);
    	
    	Class<Person> personClass = (Class<Person>)Class.forName("Person");
    	
    	Field name_field = personClass.getField("name");
    	
    	Field age_field = personClass.getDeclaredField("age");
    	age_field.setAccessible(true);
    	
    	name_field.set(person, "임꺽정");
    	age_field.set(person, 88);
    	
    	System.out.println(name_field.get(person));
    	System.out.println(age_field.get(person));
    }
    ```


### Reflection의 문제점

런타임에 동작하기 때문에, 컴파일 시점에 오류를 잡을 수 없어 모든 예외 처리를 해주어야한다는 단점이 있다.

# Test

## Junit3TestRunner, Junit4TestRunner

- Class.getConstructor를 사용해 클래스의 생성자를 가져옴
- Constructor.newInstance: 해당 클래스의 생성자를 사용해 객체를 생성
    - class.newInstance: 똑같이 객체를 생성

      매개변수가 없는 생성자만 호출 가능

      생성자가 던지는 모든 예외를 던짐

      public 생성자만 사용 가능


    Constructor.newInstance의 경우
    
    매개변수에 상관없이 생성자를 호출가능
    
    생성자가 던지는 예외를 InvocationTargetException으로 래핑
    
    private 생성자도 사용 가능 
    
    하기 때문에 Constructor의 newInstance를 주로 더 사용한다.  

- Class.getMethods를 사용해 메서드를 가져와 “test”로 시작하는 메서드를 invoke를 통해 생성된 객체에 대해 실행
- Method의 isAnnotationPresent를 통해 특정 어노테이션을 사용한 메서드를 실행

## ReflectionTest, ReflectionsTest

### Java.lang.Class

### 메서드

- getSimpleName: 클래스의 이름만 반환
- getName: 패키지명을 포함한 클래스의 이름을 반환
- GenCanonicalName: import문에 사용하는 이름을 반환

### Java.reflections.Reflections

- Reflection 객체를 생성하면서 매개변수로 패키지명을 넣어 해당 패키지에 존재하는 모든 클래스를 탐색해 읽어들일 수 있다.
    - getTypesAnnotatedWith: 특정 어노테이션을 가진 모든 클래스 탐색 가능

## AnnotationHandlerMapping

### Mockito

실제 객체를 모방한 Mock 객체 생성 가능

- when().then()

  when 안에 정해진 mock 객체의 메서드가 실행될 때 리턴하는 값을 지정해 줄 수 있다.


### AnnotationHandlerMapping initialize

- Reflections 객체를 사용해 basePackage에 Controller 어노테이션이 붙은  클래스를 읽어오기
- 클래스의 메서드에 RequestMapping 어노테이션이 붙은 메서드를 가져와 handler를 매핑해주어야 함

  클래스별로 메서드를 모두 읽어와 확인

- 해당하는 메서드에 대해 requestMapping의 값과 메서드를 가져와 handlerkey를 생성하고 새로운 HandlerExecution을 생성
- getHandler 메서드에서는 해당하는 Handlerkey에 대한 handler가 등록되어 있는지 확인하고 해당하는 handler 객체를 반환

# 스프링 MVC 프레임워크 동작 방식

예제 코드를 실행하기 위해 스프링 MVC 설정을 하고 컨트롤러와 뷰 생성을 위한 JSP 코드를 작성하기

## 1. 스프링 MVC 핵심 구성 요소

DispatcherServlet은 모든 연결을 담당하며 웹 브라우저로부터 들어온 요청을 처리하기 위한 컨트롤러 객체를 검색하는 역할을 수행하는데 직접 컨트롤러를 검색하지 않고 HandlerMapping이라는 빈 객체에서 컨트롤러 검색을 요청

HandlerMapping은 클라이언트의 요청 경로를 이용해 이를 처리할 컨트롤러 빈 객체를 DispatcherServlet에 전달

DispatcherServlet은 HandlerMapping이 찾아준 컨트롤러 객체를 처리할 수 있는 HandlerAdapter 빈에게 요청 처리를 위임하고 HandlerAdapter는 컨트롤러의 알맞은 메서드를 호출해 요청을 처리하고 그 결과를 다시 DispatcherServlet에 리턴

HandlerAdater로부터 컨트롤러의 요청 처리 결과를 ModelAndView로 받으면 DispatcherServlet은 결과를 보여줄 뷰를 찾기 위해 ViewResolver 빈 객체를 사용

ModelAndView는 컨트롤러가 리턴할 뷰 이름을 담고 있고, ViewResolver는 이 뷰 이름에 해당하는 View 객체를 찾거나 생성해 리턴

ViewResolver는 응답을 생성하기 위해 JSP를 사용하고 매번 새로운 View 객체를 생성해서 DispatherServlet에 리턴

DispatcherServlet은 ViewResolver가 리턴한 View 객체에게 응답 결과 생성을 요청하고 View 객체는 JSP를 실행해 웹 브라우저에 전송할 응답 결과 생성

### 컨트롤러와 핸들러

DispatcherServlet 입장에서 클라이언트 요청을 처리하는 객체의 타입이 반드시 @Controller를 적용한 클래스일 필요는 없음

핸들러 객체의 실제 타입에 상관없이 실행 결과를 ModelAndView라는 타입으로 받기만 한다면 상광 없음

## 2. DispatcherServlet과 스프링 컨테이너

DispatcherServlet이 사용하는 설정 파일에 반드시 HandlerMapping, HandlerAdapter, 컨트롤러 빈, ViewResolver에 대한 정의가 포함되어 있어야 함

## 3. @Controller를 위한 HandlerMapping과 HandlerAdapter

RequestMappingHandlerAdapter는 컨트롤러 메서드 결과 값이 String 타입이면 값을 뷰 이름으로 갖는 ModelAndView 객체를 생성해서 리턴

## 4. WebMvcConfigurer 인터페이스와 설정

@EnableWebMvc 애노테이션은 @Contoroller 애노테이션을 붙인 컨트롤러를 위한 설정을 생성하고 WebMvcConfigurer의 빈을 이용해 MVC 설정을 추가로 생산

```java
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer{
	
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer){
		configurer.enable();
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry){
		registry.jsp("/WEB-INF/view/", ".jsp");
	}
}
```

## 5. JSP를 위한 ViewResolver

WebMvcConfigurer 인터페이스에 저의된 configureViewResolver() 메서드는 ViewResolverRegistry 타입의 registry 파라미터를 갖는데 ViewResolverRegistry#jsp() 메서드를 통해 JSP를 위한 ViewResolver를 설정 가능

```java
@Bean
public ViewResolver viewResolver(){
	InternalResourceViewResolver vr = new InternalResourceViewResolver();
	vr.setPerfix("/WEB-INF/view/");
	vr.setSuffix(".jsp");
	return vr;
}
```

위에서 본 것처럼 컨트롤러의 실행 결과를 받은 DispatcherServlet은 ViewResolver에게 뷰 이름에 해당하는 View 객체를 요청

InternalResourceViewResolver는 “perfix + 뷰 이름 + suffix”에 해당하는 경로를 뷰 코드로 사용하는 InternalResourceView 타입의 View 객체를 리턴

DispatcherServlet은 컨트롤러의 실행 결과를 HandlerAdapter를 통해 ModelAndView 형태로 받는데 Model에 담긴 값은 View 객체에 Map 형식으로 전달

InternalResourceView는 Map 객체에 담겨 있는 키 값을 request.stAttribute()를 이용해 request의 속성에 저장하고 해당 경로의 JSP를 실행

→ 컨트롤러에 저장한 Model 속성은 request 객체 속성으로 JSP에 전달되기 때문에 JSP는 다음과 같이 모델에 지정한 속성 이름을 사용해 값을 사용 가능

## 6. 디폴트 핸들러와 HandlerMapping의 우선순위

web.xml 설정에 준 DispatchServlet에 대한 매핑 경로가 ‘/’일 경우 .jsp로 끝나는 요청을 제외한 모든 요청을 DispatchServlet이 처리

HandlerMapping은 @Controller 애노테이션을 적용한 빈 객체가 처리할 수 있는 요청 경로만 대응 가능

→ 다른 경로의 요청을 처리하기 위해 WebMvcConfigurer의 configureDefaultServletHandling() 메서드를 사용

- configureDefaultServletHandling()

  DefaultServletHandlerConfigurer#enable()은 두개의 빈 객체를 추가

    - DefaultServletHttpRequestHandler: 클라이언트의 모든 요청을 WAS가 제공하는 디폴트 서블릿에 전달
    - SimplerUrlHandlerMapping: 모든 경로를 DefaultServletHttp RequestHandler를 이용해서 처리하도록 설정

  RequestMappingHandlerMapping의 적용 우선순위가 더 높기 때문에 먼저 처리할 핸들러를 검색하고 해당 컨트롤러가 존재하지 않을 경우 DispatcherServlet은 DefaultServletHttpRequestHandler에 처리를 요청


## 7. 직접 설정 예

@EnableWebMvc 애노테이션을 사용하지 않고 스프링 MVC를 사용하는 것도 가능