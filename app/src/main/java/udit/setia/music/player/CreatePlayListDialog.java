package udit.setia.music.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abhiandroid.Activities.R;

/**
 * Created by uditsetia on 24/1/18.
 */

public class CreatePlayListDialog extends AppCompatActivity {

	@Override
	protected void onCreate (@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_playlist_dialog);

		final EditText etPlayListname = (EditText) findViewById(R.id.et_playlist_name);
		Button btnCancel = (Button) findViewById(R.id.btn_cancel);
		Button btnOk = (Button) findViewById(R.id.btn_ok);

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				finish();
			}
		});

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				String playListName = etPlayListname.getText().toString();
				if (playListName.isEmpty()) {
					//Toast.makeText(CreatePlayListDialog.this, "Please enter a name for playlist", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(CreatePlayListDialog.this, SelectSongs.class);
					intent.putExtra("playListName", playListName);
					startActivity(intent);
					finish();
				}
			}
		});

	}
}
