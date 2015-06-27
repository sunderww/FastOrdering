//
//  DishCell.h
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Dish.h"

@interface DishCell : UITableViewCell {
  IBOutlet UILabel *      mainLabel;
}

- (void)setDish:(Dish *)dish andTag:(NSUInteger)tag;
- (void)setQuantity:(NSInteger)quantity;

@property (nonatomic, strong) UITextField *           textField;
@property (nonatomic, retain) id<UITextFieldDelegate> delegate;

@end
