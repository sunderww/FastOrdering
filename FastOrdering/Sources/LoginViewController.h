//
//  LoginViewController.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LoaderView.h"
#import "SocketHelper.h"

@interface LoginViewController : UIViewController <SocketIODelegate> {
    IBOutlet UITextField *  keyField;
    IBOutlet UIButton *     loginButton;
	IBOutlet LoaderView *	loader;
	
	BOOL					waitingResponse;
}

- (IBAction)login;

@end
