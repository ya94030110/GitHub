using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ScoreResult : MonoBehaviour {

	public Text ScoreUI;
	void Start () {
		ScoreUI.text = "Score:    " + ScoreManager._instance.PlayerScore.ToString();
	}

}
