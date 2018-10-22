Pokazac, ze do implementacji semafora za pomoca metod wait i notify nie wystarczy instrukcja if tylko potrzeba uzyc while . 
Wyjasnic teoretycznie dlaczego i potwierdzic eksperymentem w praktyce. 
(wskazowka: rozwazyc dwie kolejki: czekajaca na wejsciedo monitora obiektu oraz kolejke zwiazana z instrukcja wait , 
rozwazyc kto kiedy jest budzony i kiedy nastepuje wyscig). (1 pkt.)

Sam while jest ważny głownie dla wait().
W Javie wątek może zostac obudzony jeśli inny wątek nadał notify()/ nofifyAll(), ale nie jest to gwarancją,
że warunek wciąż jest spełniony. Inny wątek też może zostać obudzony i "wyprzedzić" aktualny. Innymi słowy:
stan w którym wątek się obudzi może być już zaktualizowany względem tego, który umożliwił wywołanie notify()

dla kodu:
```scala
    val binarySemaphoreTest = new BinarySemaphoreTest
    binarySemaphoreTest.test(100,2000, new BinarySemaphore)

    binarySemaphoreTest.test(100,2000,new BinarySemaphoreWithoutLoop)
```
Wynik to:

```
Number of threads: 100
result: 200000
Should be: 200000
Number of threads: 100
result: 199847
Should be: 200000
```