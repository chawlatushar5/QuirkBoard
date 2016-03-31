//
//  FirstViewController.swift
//  Practise
//
//  Created by Tushar Chawla on 3/8/16.
//  Copyright © 2016 Tushar Chawla. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation

class FirstViewController: UIViewController, MKMapViewDelegate , CLLocationManagerDelegate{
    
    @IBOutlet weak var label: UITextField!
    @IBOutlet weak var locationuser: MKMapView!

    
    @IBOutlet weak var button: UIButton!
    
    var labeltext = String()
    let locationmanager = CLLocationManager()
    
    // location of the user
    
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        let location = locations.last
        let center = CLLocationCoordinate2D(latitude: location!.coordinate.latitude, longitude: location!.coordinate.longitude)
        let region = MKCoordinateRegion(center: center, span: MKCoordinateSpan(latitudeDelta: 1, longitudeDelta: 1))
        self.locationuser.setRegion(region, animated: true)
        self.locationmanager.stopUpdatingLocation()
        label.text=String(location!.coordinate.latitude)
        
    }
    func locationManager(manager: CLLocationManager, didFailWithError error: NSError) {
        print("ERRORS: " + error.localizedDescription)
    }

    
   
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        self.locationmanager.delegate = self
        self.locationmanager.desiredAccuracy=kCLLocationAccuracyBest
        self.locationmanager.requestWhenInUseAuthorization()
        self.locationmanager.startUpdatingLocation()
        self.locationuser.showsUserLocation=true

        if labeltext==""{
        }
        else{
            
        // Do any additional setup after loading the view, typically from a nib.
            label.backgroundColor=UIColor(red:1.00, green:0.876 ,blue:0.162 , alpha:1.00)
            label.opaque=true
            label.text=labeltext
            label.endEditing(true)
            
            
            label.textAlignment = NSTextAlignment.Center
            
        }
    }
   
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
        // Dispose of any resources that can be recreated.
    }
    
    
    
}
