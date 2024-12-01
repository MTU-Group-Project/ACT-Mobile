package mtu.gp.actmobile.worker

import android.content.Context

//class PriceAlertWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
//    override suspend func doWork(): Result {
//        return try {
//            for (i in 0..10) {
//                setProgress(workDataOf("progress" to i * 30))
//                val result = performTask(i)
//                delay(1000)
//                Log.d("", "Progress $i")
//            }
//        } catch (e: Exception) {
//            Log.d("", "Error")
//            Result.failure()
//        }
//    }
//
//    private fun performTask(i: Int): Int {
//        return i*i
//    }
//}