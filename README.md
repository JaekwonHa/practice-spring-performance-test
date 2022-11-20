# practice-spring-performance-test

Spring 아키텍처에 따른 성능 테스트

* spring-mvc
* spring-mvc-coroutine
* spring-webflux-mvc 			(reactor)
* spring-webflux-mvc-coroutine
* spring-webflux-router 		(reactor)
* spring-webflux-corouter

## locust 환경 구성

```python
# IDE 에서도 python env 환경을 추가할 수 있다

# 수동 설치
conda create -n practice-spring-performance-test python=3.10
conda activate practice-spring-performance-test
pip install locust

# 실행
locust -f locustfile_write.py --web-port=18080
locust -f locustfile_read.py --web-port=18081
```
