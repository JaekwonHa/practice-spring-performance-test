from locust import HttpUser, task, between


class LoadTestUser(HttpUser):
    wait_time = between(1, 2.5)

    # @task
    # def create_person(self):
    #     self.client.post(f'/person')

    @task
    def get_person(self):
        self.client.get('/person')
