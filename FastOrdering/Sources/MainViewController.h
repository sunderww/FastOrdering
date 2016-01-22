//
//  MainViewController.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SocketIO.h"
#import "SyncHelper.h"
#import "LoaderView.h"
#import "Order.h"
#import "SocketHelper.h"

@interface MainViewController : UIViewController <SyncerDelegate, UITableViewDataSource, UITableViewDelegate, SocketEventListener> {
    IBOutletCollection(UIButton) NSArray *  panelButtons;

    IBOutlet UIButton *     logOutButton;
    IBOutlet UIButton *     mainButton;
    IBOutlet UIButton *     takeOrderButton;
    IBOutlet UIButton *     historyButton;
    IBOutlet UIButton *     notificationsButton;
    IBOutlet UIButton *     aboutButton;
    IBOutlet UIButton *     panelButton;
    IBOutlet UIView *       panelView;
    IBOutlet UIView *       centralView;
    IBOutlet UIView *       overlay;
    IBOutlet UILabel *      titleLabel;
	IBOutlet UIButton *		syncButton;

    IBOutlet UIView *       mainView;
    IBOutlet UILabel *      lastOrdersLabel;
    IBOutlet UILabel *      lastNotificationsLabel;
    IBOutlet UILabel *      noOrderLabel;
    IBOutlet UILabel *      noNotificationLabel;
    IBOutlet UITableView *  lastOrdersTableView;
    IBOutlet UITableView *  lastNotificationsTableView;
    IBOutlet UIButton *     orderButton;
    IBOutlet LoaderView *   loaderView;

    BOOL                    panelShown;
	BOOL					syncing;
    NSUInteger              classesToSync;
    UIViewController *      controller;
    NSArray *               lastOrders;
    NSArray *               lastNotifications;
    NSTimer *               timer;
	BOOL					hasLoaded;
}

@property (nonatomic, assign) BOOL	doNotSync;

- (IBAction)buttonClicked:(id)sender;
- (IBAction)logOut;
- (IBAction)showPanel;
- (IBAction)reSync;

- (void)goToOrder:(Order *)order;
- (void)goBackToMainPage;
- (void)reloadData;
- (BOOL)onMainPage;

@end
