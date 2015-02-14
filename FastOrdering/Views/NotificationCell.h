//
//  NotificationCell.h
//  FastOrdering
//
//  Created by Sunder on 14/02/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Notification.h"

@interface NotificationCell : UITableViewCell {
    IBOutlet UILabel *  mainLabel;
    IBOutlet UILabel *  descLabel;
}

- (void)setNotification:(Notification *)notif;

@end
