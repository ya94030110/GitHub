using UnityEngine;
using System.Collections;

public class AutoGenerate : MonoBehaviour {

	public Transform enemy;
	public float createHeight = 5f;
	public float Xmin = -10f;
	public float Xmax = 10f;
	public float createRate = 3f;

	void Start () {
		//2秒後開始，之後每createRate秒產生
		InvokeRepeating("CreateEnemy", 2f, createRate);
	}

	void CreateEnemy(){
		//隨機X軸位置產生敵人
		Instantiate(enemy, new Vector3(Random.Range(Xmin,Xmax), createHeight, 0f), enemy.rotation);
	}
}
