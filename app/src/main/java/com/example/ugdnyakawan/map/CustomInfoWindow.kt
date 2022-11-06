package com.example.ugdnyakawan.map
import com.example.ugdnyakawan.R
import kotlinx.android.synthetic.main.layout_tooltip.view.*
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

//Penghubung ke layout tooltip(tooltip:pesan singkat)
class CustomInfoWindow(mapView: MapView?) : InfoWindow(R.layout.layout_tooltip, mapView) {
    override fun onClose() {
        //by default, do nothing
    }

    override fun onOpen(item: Any) {
        val marker = item as Marker
        val infoWindowData = marker.relatedObject as ModelMain

        val tvNamaLokasi = mView.tvNamaLokasi
        val tvAlamat = mView.tvAlamat
        val imageClose = mView.imageClose

        tvNamaLokasi.text = infoWindowData.strName
        tvAlamat.text = infoWindowData.strVicinity
        imageClose.setOnClickListener {
            marker.closeInfoWindow()
        }
    }
}


