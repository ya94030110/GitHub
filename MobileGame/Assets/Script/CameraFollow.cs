using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraFollow : MonoBehaviour {
	public GameObject Cat;

	void Start()
	{
		transform.position = new Vector3(Cat.transform.position.x + 8, 0, -10);
	}

	// Update is called once per frame
	void Update () {
		transform.position = new Vector3(Cat.transform.position.x + 8, 0, -10);
	}
}
