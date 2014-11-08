//
//  LoginViewController.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginViewController : UIViewController {
    IBOutlet UITextField *  emailField;
    IBOutlet UITextField *  passwordField;
    IBOutlet UIButton *     loginButton;
}

- (IBAction)login;

@end
