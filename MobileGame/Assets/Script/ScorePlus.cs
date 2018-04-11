using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ScorePlus : MonoBehaviour {
	public int score;
	bool firstEnter = false;
	// Use this for initialization
	void Start () {
		firstEnter = false;
	}

	void OnCollisionEnter2D(Collision2D col)
	{
		if (firstEnter == false) {
			if (col.gameObject.tag == "Player") {
				ScoreManager._instance.PlayerScore += score;
				firstEnter = true;
			}
		}
	}

}
