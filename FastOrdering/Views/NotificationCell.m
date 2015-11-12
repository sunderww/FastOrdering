//
//  NotificationCell.m
//  FastOrdering
//
//  Created by Sunder on 14/02/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "NotificationCell.h"

@implementation NotificationCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setNotification:(Notification *)notif {
    NSDateFormatter * f = [NSDateFormatter new];
    
    f.dateFormat = @"'Le' dd/MM/yy à HH:mm";
    mainLabel.text = [NSString stringWithFormat:@"Table #%@ : %@", notif.numTable, notif.msg];
    descLabel.text = [f stringFromDate:notif.date];
}

@end
