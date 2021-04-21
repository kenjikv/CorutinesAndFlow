package com.kawaida.coroutineskotlin

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    //blockingExample()
//    suspendExample()
//    suspendExample2()
//    dispatcher()
//    launch()
//    exampleJob()
//    Thread.sleep(10000)
//    asyncAwait()
//    asyncAwaitDeferred()
//    println(measureTimeMillis { asyncAwait() }.toString())
//    println(measureTimeMillis { asyncAwaitDeferred() }.toString())
//    println(measureTimeMillis { withContextIO() }.toString())
//    cancelCoroutine()
}


//Bloquear
//Bloquea el main de la aplicacion (Bloquea el main thread)
fun longTaskWithMessage(message: String) {
    Thread.sleep(4000)
    println(message + Thread.currentThread().name)
}

fun blockingExample() {
    println("Tarea 1 " + Thread.currentThread().name)
//    longTaskWithMessage("Tarea 2 ")
//    delayCoroutine("Tarea 2 ")
    println("Tarea 3 " + Thread.currentThread().name)
}

//Suspender
//Nos permitira suspender una coroutine, es una funcion q se puede pausar y reanudar luego
suspend fun delayCoroutine(message: String) {
    delay(timeMillis = 4000)
    println(message + Thread.currentThread().name)
}

//Constructores de coroutines
//runblocking bloquea el hilo, nos permite ejecutar una coroutine pero bloquea el hilo principal
//o en el hilo que estemos
fun suspendExample() {
    println("Tarea 1 " + Thread.currentThread().name)
    runBlocking {
        delayCoroutine("Tarea 2 ")
    }
    println("Tarea 3 " + Thread.currentThread().name)
}

fun suspendExample2() = runBlocking {
    println("Tarea 1 " + Thread.currentThread().name)
    delayCoroutine("Tarea 2 ")
    println("Tarea 3 " + Thread.currentThread().name)
}

//Dispatchers
//Se puede indicar en que hilo se desea que se ejecute
fun dispatcher() {
    //si no se indica el hilo se ejecuta en el main
    runBlocking {
        println("Hilo en el que se ejecuta 1 ${Thread.currentThread().name}")
    }

    //Si no nos importa en que hilo se va ejecutar se coloca en este
    runBlocking(Dispatchers.Unconfined) {
        println("Hilo en el que se ejecuta 2 ${Thread.currentThread().name}")
    }
    runBlocking(Dispatchers.Default) {
        println("Hilo en el que se ejecuta 3 ${Thread.currentThread().name}")
    }
    //operaciones de entrada y salida de datos ws o bases de datos
    runBlocking(Dispatchers.IO) {
        println("Hilo en el que se ejecuta 4 ${Thread.currentThread().name}")
    }
    //creo mi propio hilo
    runBlocking(newSingleThreadContext("MiHilo")) {
        println("Hilo en el que se ejecuta 5 ${Thread.currentThread().name}")
    }
    //Con android o el main thread
//    runBlocking(Dispatchers.Main) {
//        println("Hilo en el que se ejecuta 5 ${Thread.currentThread().name}")
//    }
}

//launch otra forma de crear coroutines, a diferencia del runblocking no se bloquea
// el hilo en el que esta ejecutando
fun launch() {
    println("Tarea 1 " + Thread.currentThread().name)
    GlobalScope.launch {
        delayCoroutine("Tarea 2 ")
    }
    println("Tarea 3 " + Thread.currentThread().name)
    Thread.sleep(10000)
}

//globalscope = coroutine scope nos ayuda a definir el ciclo de vida de la coroutine, podria estar ligado
//al ciclo de vida de un activity, fragment o de toda la aplicacion, globalscope esta asociada a toda la
//vida de la aplicacion

//Job es un elemento cancelable que su ciclo de vida culmina en su finalizacion
fun exampleJob() {
    println("Tarea 1 " + Thread.currentThread().name)
    val job = GlobalScope.launch {
        delayCoroutine("Tarea 2 ")
    }
    println("Tarea 3 " + Thread.currentThread().name)
    job.cancel()
}

suspend fun calculateHard(): Int {
    delay(2000)
    return 15
}

//async and await = nos devuelve un valor, en este caso nos devuelve un defered
fun asyncAwait() = runBlocking {
    val numero1: Int = async { calculateHard() }.await()
    val numero2: Int = async { calculateHard() }.await()
    val resultado = numero1 + numero2
    println(resultado.toString())
}

//deferred = es un futuro cancelable sin bloqueos
fun asyncAwaitDeferred() = runBlocking {
    val numero1: Deferred<Int> = async { calculateHard() }
    val numero2: Deferred<Int> = async { calculateHard() }
    val resultado: Int = numero1.await() + numero2.await()
    println(resultado.toString())
}

fun withContextIO() = runBlocking {
    val numero1 = withContext(Dispatchers.IO){ calculateHard() }
    val numero2 = withContext(Dispatchers.IO){ calculateHard() }
    val resultado: Int = numero1 + numero2
    println(resultado.toString())
}

fun cancelCoroutine(){
    runBlocking {
        val job = launch {
            repeat(1000){
                i ->
                println("job: $i")
                delay(500L)
            }
        }
        delay(1400)
        job.cancel()
        println("main: cansado de esperar")
    }
}


