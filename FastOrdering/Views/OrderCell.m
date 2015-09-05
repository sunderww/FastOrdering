//
//  OrderCell.m
//  FastOrdering
//
//  Created by Sunder on 14/02/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "OrderCell.h"
#import "Table.h"

@implementation OrderCell

- (void)awakeFromNib {
  // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
  [super setSelected:selected animated:animated];
  
  // Configure the view for the selected state
}

- (void)setOrder:(Order *)order {
  NSDateFormatter * f = [NSDateFormatter new];
  
  f.dateFormat = @"'Le' dd/MM/yy 'à' HH:mm";
  NSString * table = order.table.name ? [NSString stringWithFormat:@"table #%@, ", order.table.name] : @"";
  NSString * orderId = [NSString stringWithFormat:@"%@...%@", [order.serverId substringToIndex:4], [order.serverId substringFromIndex:order.serverId.length - 4]];
  mainLabel.text = [NSString stringWithFormat:@"Order #%@, %@PA:%@", orderId, table, @"?"];
  descLabel.text = [f stringFromDate:order.updatedAt];
}

@end
