package se.kth.binyam.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AnimationActivity extends Activity {
 
	private ImageView spaceshipImage;
	private Animation hyperspaceJumpAnimation;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		spaceshipImage = (ImageView) findViewById(R.id.HyperspaceImageView);
		hyperspaceJumpAnimation = 
			AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
		
	}

	public void onHyperspaceJumpClick(View view) {
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	}
}