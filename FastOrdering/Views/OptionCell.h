//
//  OptionCell.h
//  FastOrdering
//
//  Created by Sunder on 06/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Option.h"

@interface OptionCell : UITableViewCell {
	IBOutlet UILabel *      mainLabel;
}

- (void)setOption:(Option *)option andTag:(NSUInteger)tag;
- (void)setQuantity:(NSInteger)quantity;

@property (nonatomic, strong) IBOutlet UITextField *	textField;

@end
