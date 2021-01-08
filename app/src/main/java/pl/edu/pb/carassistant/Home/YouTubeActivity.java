package pl.edu.pb.carassistant.Home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import pl.edu.pb.carassistant.R;

public class YouTubeActivity extends YouTubeBaseActivity {

    LinearLayout motodoradcaChannel, kicksterChannel;

    Context context;

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    public class Config {
        private Config() {}
        public static final String DEVELOPER_KEY="AIzaSyCkabbDMoBRBytn53D2v_W4f9SaQ4XWJt0";
        public static final String VIDEO_ID="2aPb2Xvlfuw";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);

        context = getApplicationContext();

        youTubePlayerView = findViewById(R.id.youtube_player);

        motodoradcaChannel = findViewById(R.id.layout_motodoradca);
        kicksterChannel = findViewById(R.id.layout_kickster);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.cueVideo(Config.VIDEO_ID);

                //youTubePlayer.setFullscreen(true);
                //youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                //youTubePlayer.cuePlaylist("PL4o29bINVT4EG_y-k5jGoOu3-Am8Nvi10");
                //youTubePlayer.loadVideo("2aPb2Xvlfuw");
                //youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(Config.DEVELOPER_KEY, onInitializedListener);

        motodoradcaChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/user/motoopiniecom"));
                intent.setPackage("com.google.android.youtube");

                CheckYouTube(intent);
            }
        });

        kicksterChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/channel/UCE59JfgJn06qFVHES99M_KQ"));
                intent.setPackage("com.google.android.youtube");

                CheckYouTube(intent);
            }
        });
    }

    private void CheckYouTube(Intent intent)
    {
        PackageManager manager = getPackageManager();
        List<ResolveInfo> info = manager.queryIntentActivities(intent, 0);
        if (info.size() > 0) {
            startActivity(intent);
        } else {
            Toast.makeText(context, getResources().getString(R.string.youtube_app_error), Toast.LENGTH_LONG).show();
        }
    }
}