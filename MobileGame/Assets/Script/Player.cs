using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    public Generate generater;
	public Animator anim;
	public GameObject deathAudio;
	public GameObject scoreAudio;
    public int score = 1;
    bool firstcol = true;
    float jumpForce = 300f;
    public Vector2 rebornPosition = new Vector2(-12f, -1.5f);
    public Vector2 unitForce = new Vector2(1200f, 600f);
    float groundY;
    float timer;
    float lastColCenter;
    // Use this for initialization
    void Start()
    {
		firstcol = true;
        GetComponent<Rigidbody2D>().velocity = new Vector2(0, 9f);
        groundY = rebornPosition.y;
        lastColCenter = -10000f;
		anim.Play ("Wolf_Idle");

    }

    // Update is called once per frame
    void Update()
    {
		if (GetComponent<Rigidbody2D> ().velocity == new Vector2 (0, 0) && Input.touches.Length == 0) {
			GetComponent<Rigidbody2D>().velocity = new Vector2(0, 9f);
			anim.Play ("Wolf_Idle");
            //groundY = GetComponent<Rigidbody2D>().transform.position.y;

		}
        //if (Input.GetKeyDown(KeyCode.Space)) print("Space");
        if(Input.touches.Length == 1)
        {
            if (Input.touches[0].phase == TouchPhase.Began && GetComponent<Rigidbody2D>().velocity.x == 0)
            {
                //GetComponent<Rigidbody2D>().AddForce(new Vector2(jumpForce, 0f));
                anim.Play("Wolf_dig");
                float x = GetComponent<Rigidbody2D>().transform.position.x;
                GetComponent<Rigidbody2D>().transform.position = new Vector2(x, groundY);
                GetComponent<Rigidbody2D>().velocity = new Vector2(0, 0);
                timer = 0f;
                if (firstcol) firstcol = false;
            }
            if ((Input.touches[0].phase == TouchPhase.Moved || Input.touches[0].phase == TouchPhase.Stationary) && GetComponent<Rigidbody2D>().velocity.x == 0) timer += Time.deltaTime;
            if (Input.touches[0].phase == TouchPhase.Ended && GetComponent<Rigidbody2D>().velocity.x == 0)
            {
                GetComponent<Rigidbody2D>().AddForce(timer * unitForce);
                anim.Play("Wolf_jump");
            }
        }
        
	}

    void OnCollisionEnter2D(Collision2D col)
    {
        if (col.gameObject.tag == "ground")
        {
			Instantiate (deathAudio, this.transform.position, Quaternion.identity);
			Application.LoadLevel ("GameOver");
            groundY = rebornPosition.y;
            firstcol = true;
            lastColCenter = -10000f;
            timer = 0f;
        }
        else
        {
            if(onTop(col))
            {
                groundY = GetComponent<Rigidbody2D>().transform.position.y;
                addScore(col);
                timer = 0f;
            }
            float y = GetComponent<Rigidbody2D>().velocity.y;
            GetComponent<Rigidbody2D>().velocity = new Vector2(0, y);
        }
        
    }
    bool onTop(Collision2D col)
    {
        Collider2D collider = col.collider;
        Vector3 contactPoint = col.contacts[0].point;
        Vector3 center = collider.bounds.center;
        float RectWidth = collider.bounds.size.x;
        if (contactPoint.y > center.y && (contactPoint.x <= center.x + RectWidth / 2 && contactPoint.x >= center.x - RectWidth / 2) && collider.GetType() == typeof(BoxCollider2D))
            return true;
        else return false;
    }
    void addScore(Collision2D col)
    {
        //print("center = " + lastColCenter);
        Collider2D collider = col.collider;
        if (lastColCenter == -10000f)
        {
            lastColCenter = collider.bounds.center.x;
            return;
        }
        else if (collider.bounds.center.x == lastColCenter) return;
        else
        {
			Instantiate (scoreAudio, this.transform.position, Quaternion.identity);
            lastColCenter = collider.bounds.center.x;
            ScoreManager._instance.PlayerScore += score;
            generater.work();
        }
    }
}
