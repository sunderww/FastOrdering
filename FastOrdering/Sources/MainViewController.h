//
//  MainViewController.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SyncHelper.h"

@interface MainViewController : UIViewController <SyncerDelegate> {
    IBOutlet UIButton *     logOutButton;
    IBOutlet UIButton *     mainButton;
    IBOutlet UIButton *     tableButton;
    IBOutlet UIButton *     menuButton;
    IBOutlet UIButton *     historyButton;
    IBOutlet UIButton *     notificationsButton;
    IBOutlet UIButton *     aboutButton;
    IBOutlet UIButton *     panelButton;
    IBOutlet UIView *       panelView;
    IBOutlet UIView *       centralView;
    IBOutlet UIView *       overlay;
    IBOutlet UILabel *      titleLabel;

    BOOL                    panelShown;
    NSUInteger              classesToSync;
    UIViewController *      controller;
}

- (IBAction)buttonClicked:(id)sender;
- (IBAction)logOut;
- (IBAction)showPanel;

@end
