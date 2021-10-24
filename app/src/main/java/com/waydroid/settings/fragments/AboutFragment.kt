package com.waydroid.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.waydroid.settings.R
import com.waydroid.settings.databinding.FragmentAboutBinding
import com.waydroid.settings.utils.FileDownloader
import io.noties.markwon.Markwon
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AboutFragment.
         */
        @JvmStatic
        fun newInstance() = AboutFragment()
    }

    private lateinit var binding: FragmentAboutBinding
    private lateinit var readmeMD: File
    private var isDownloaded = false
    private var isLoaded = false
    private var disposable = Disposables.disposed()

    private val fileDownloader by lazy {
        FileDownloader(
            OkHttpClient.Builder().build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readmeMD = File("${requireContext().getExternalFilesDir(null)!!.absolutePath}/README.md")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.fragment_about, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (isDownloaded) loadMDViewer()
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable = fileDownloader.download(
            "https://raw.githubusercontent.com/waydroid/docs/master/README.md", readmeMD)
            .throttleFirst(2, TimeUnit.SECONDS)
            .toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread())
            .subscribe({
                // Downloading progress
            }, {
                // Download failed
                it.printStackTrace()
            }, {
                // Downloaded
                isDownloaded = true
                // load MD file only if the fragment is visible
                if (isVisible) loadMDViewer()
            })
    }

    /**
     * Hides the progress bar & load the MD file
     */
    private fun loadMDViewer() {
        if (!isLoaded) {
            lifecycleScope.launch {
                // read content of MD file as an string in an IO thread
                val contents = withContext(Dispatchers.IO) {
                    readmeMD.readText()
                }
                // Using Main/UI Thread set the views
                withContext(Dispatchers.Main) {
                    binding.apply {
                        markdownView.apply {
                            Markwon.create(this.context).setMarkdown(this, contents)
                            visibility = View.VISIBLE
                        }
                        progressBar.visibility = View.GONE
                    }
                }
            }
            isLoaded = true
        }
    }
}
