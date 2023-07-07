package com.lrm.blur_o_matic.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lrm.blur_o_matic.OUTPUT_PATH
import java.io.File
import java.lang.Exception

private const val TAG = "CleanupWorker"
class CleanupWorker(ctx: Context, params: WorkerParameters): Worker(ctx, params) {

    override fun doWork(): Result {
        makeStatusNotification("Cleaning up temporary files", applicationContext)
        sleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if(entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.e(TAG, "Deleted $name - $deleted")
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }

}