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
    
    f.dateFormat = @"Le dd/MM/yy à HH:mm";
    mainLabel.text = [NSString stringWithFormat:@"Order #XX, Table #%@, PA:X", order.table.name];
    descLabel.text = [f stringFromDate:order.updatedAt];
}

@end
