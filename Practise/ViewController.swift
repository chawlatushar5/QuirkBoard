//
//  ViewController.swift
//  Practise
//
//  Created by Tushar Chawla on 3/4/16.
//  Copyright © 2016 Tushar Chawla. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var posttext: UITextField!
    
    
    var message:String!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func Postitbutton(sender: AnyObject) {
      
        message=posttext.text
        self.posttext.resignFirstResponder()
        
    }
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let destinationViewController: FirstViewController = segue.destinationViewController as! FirstViewController
        destinationViewController.labeltext = message
    }
    //to make the keyboard disappear
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }

}

