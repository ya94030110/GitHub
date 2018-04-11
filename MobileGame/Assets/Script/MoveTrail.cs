using UnityEngine;
using System.Collections;

public class MoveTrail : MonoBehaviour {
	
	public int moveSpeed = 150;
	
	// Update is called once per frame
	void Update () {
		//往右移動
		transform.Translate (Vector3.right * Time.deltaTime * moveSpeed);
		//1秒後刪除
		Destroy (gameObject, 1);
	}
}
