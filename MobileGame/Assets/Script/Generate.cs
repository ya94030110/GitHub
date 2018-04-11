using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Generate : MonoBehaviour {
	public GameObject [] buildings = new GameObject[10];////(1)

 	static GameObject background;
/*  static GameObject tower;
    static GameObject house;
    static GameObject tree;
    static GameObject telephone;
*/   
	static GameObject road;
    float z = -3.85f;
    float[] y;
    public float minDelta = 9.5f;
    public float maxDelta = 13.0f;
    float lastX;
//    GameObject[] buildings;
//    int buildingNum = 4;

    int roadX = 2;
    int roadDelta = 14;
	int backX = -16;
	int backDelta = 41;

	public GameObject player;
	float lastPlayPosition;
	float lastBackPosition;

    int count = 0;
    // Use this for initialization
    void Start () {
        lastX = -14f;
/*	    tower = GameObject.Find("tower");
	    house = GameObject.Find("house");
	    tree = GameObject.Find("tree");
	    telephone = GameObject.Find("telephone");
	   
		
	    buildings = new GameObject[] { tower, house, tree, telephone };
   		y = new float[] {-2f, -2f, -3f, -1.5f };
 */
		background = GameObject.Find("Background");
		road = GameObject.Find("road");
        work();
        work();
        work();
        work();
		lastPlayPosition = player.transform.position.x;
    }
	
	// Update is called once per frame
	void Update () {
		if (player.transform.position.x - lastPlayPosition > 1.5f) {
			lastPlayPosition = player.transform.position.x;
			work();
		
		}
	}
    public void work()
    {
		int index = Random.Range(0, buildings.Length-1);
        float DeltaX = Random.Range(minDelta, maxDelta);
        lastX = lastX + DeltaX;
        roadX = roadX + roadDelta;
		backX = backX + backDelta;
        Instantiate(buildings[index], new Vector3(lastX, -3.1f, z), buildings[index].transform.rotation);
        Instantiate(road, new Vector3(roadX, -6.34f, -0.15f), road.transform.rotation);
		Instantiate(background, new Vector3(backX, 0, 0), background.transform.rotation);
    }
}
