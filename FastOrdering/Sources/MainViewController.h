//
//  MainViewController.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SocketIO.h"

@interface MainViewController : UIViewController <SocketIODelegate> {
    IBOutlet UIButton *     logOutButton;
    IBOutlet UIButton *     tableButton;
    IBOutlet UIButton *     menuButton;
    IBOutlet UIButton *     historyButton;
    IBOutlet UIButton *     notificationsButton;
    
    SocketIO *              socket;
}

- (IBAction)buttonClicked:(id)sender;
- (IBAction)logOut;

@end
