using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ScoreManager : MonoBehaviour {

	public static ScoreManager _instance;
	public int PlayerScore = 0;
	public Text ScoreUI;
	void Awake(){
		_instance = this;	
	}
	// Update is called once per frame
	void Update () {
		ScoreUI.text = "Score: "+ PlayerScore.ToString();
	}
}

