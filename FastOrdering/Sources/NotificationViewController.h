//
//  NotificationViewController.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NotificationViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
    IBOutlet UITableView *  tableView;
	IBOutlet UILabel *		noNotificationLabel;
	IBOutlet UIButton *		deleteButton;
    
    NSArray *   data;
	NSUInteger	selectCount;
}

- (IBAction)deleteSelectedNotifications;

@end
