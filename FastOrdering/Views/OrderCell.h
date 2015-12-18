//
//  OrderCell.h
//  FastOrdering
//
//  Created by Sunder on 14/02/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Order.h"

@interface OrderCell : UITableViewCell {
  IBOutlet UILabel *      mainLabel;
  IBOutlet UILabel *      descLabel;
  IBOutlet UIImageView *  imageView;
}

- (void)setOrder:(Order *)order;

@end
