package com.project.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.project.control.Utils;
import com.project.model.Comic;

public class DetailInfoTab extends Activity {
	String id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_info);
		//bundle
		Bundle bundle = getIntent().getExtras();
		if(bundle!= null){
			id = bundle.getString("id".toString());

		}
		Comic[] comic = Comic.getAllComic(Comic.url_detail_comic+id);
		Typeface tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Bold.otf");
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Regular.otf");

		/*	TextView tx1 = (TextView) findViewById(R.id.credit);
		TextView tx2 = (TextView) findViewById(R.id.writtertext);
		TextView tx3 = (TextView) findViewById(R.id.writter);
		TextView tx4 = (TextView) findViewById(R.id.artisttext);
		TextView tx5 = (TextView) findViewById(R.id.artist);
		TextView tx6 = (TextView) findViewById(R.id.storytext);
		TextView tx7 = (TextView) findViewById(R.id.story);*/
		TextView txtCredit = (TextView) findViewById(R.id.credit);
		TextView txtWritterText = (TextView) findViewById(R.id.writtertext);
		TextView txtWritter = (TextView) findViewById(R.id.writter);
		TextView txtArtistText  = (TextView) findViewById(R.id.artisttext);
		TextView txtArtist= (TextView) findViewById(R.id.artist);
		TextView txtStoryText = (TextView) findViewById(R.id.storytext);
		TextView txtStory = (TextView) findViewById(R.id.story);

		/*tx1.setTypeface(tf1);
		tx2.setTypeface(tf1);
		tx3.setTypeface(tf1);
		tx4.setTypeface(tf1);
		tx5.setTypeface(tf1);
		tx6.setTypeface(tf1);
		tx7.setTypeface(tf1);*/

		txtCredit.setTypeface(tf_bold);
		txtWritterText.setTypeface(tf_bold);
		txtWritter.setTypeface(tf_bold);
		txtArtist.setTypeface(tf_bold);
		txtArtistText.setTypeface(tf_bold);
		txtStoryText.setTypeface(tf_bold);
		txtStory.setTypeface(tf);

		txtWritter.setText(comic[0].getWritter());
		txtArtist.setText(comic[0].getArtist());
		txtStory.setText(comic[0].getInfo());

		/*	if (oComic.idComic == 1)
		{
			tx3.setText("GO KING");
			tx5.setText("MATTO");
			tx7.setText("Merasa tidak tahan dengan deadline dan tekanan kerja, Budi, seorang editor tayangan berita di sebuah stasiun televisi swasta berniat mengundurkan diri.\r\n" +
					"Baginya, cukup sudah, resign adalah keputusan yang tidak dapat ditawar-tawar lagi! \r\n" +
					"Sementara bagi Arum, reporter pemula yang belum lama bekerja merasa ancaman menjadi presenter acara kuis tak mendidik terus menghantui! Apa yang terjadi ketika keduanya tak sengaja berinteraksi?\r\n\r\n" +
					"Diangkat dari pengalaman nyata kehidupan para pekerja berita media elektronik di sebuah stasiun televisi swasta. Bagaimana suka duka mereka menghadirkan berita.");

		} else if (oComic.idComic == 2)
		{
			tx3.setText("SWETA KARTIKA");
			tx5.setText("SWETA KARTIKA");
			tx7.setText("Komik action komedi yang bercerita tentang sebuah tim pembela kebenaran yang terdiri dari empat orang dengan latar belakang berbeda dan cara berbeda pula dalam membasmi kejahatan.\r\n\r\n" +
					"Sebagai superhero, mereka tidak tampil dengan kostum ketat berwarna yang heroik khas superhero, justru hanya bermodalkan pakaian sehari-hari, topeng berwajah kera dan percaya diri yang berlebihan. " +
					"Berbagai hal jenaka dan aksi petualangan adalah sajian utama dari komik karya alumnus Seni Rupa ITB ini yang ternyata penuh intrik dan kendala. " +
					"Lima menit sebelum berita tayang kadang menjadi penentu.");
		}*/

	}
}
